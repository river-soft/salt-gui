import React, {Component} from 'react';
import Input from 'muicss/lib/react/input';
import Checkbox from 'muicss/lib/react/checkbox';
import Button from 'muicss/lib/react/button';

export default class JobAllResults extends Component {

    constructor(props) {
        super(props);

        this.state = {
            filterList: [],
            filterValue: '',
            showDetails: false,
            checkedList: []
        };
    }

    componentDidUpdate() {
        if (this.props.clearFilter) {

            let input = document.getElementById('filter');
            if (input) {
                input.value = '';
                input.classList.remove('mui--is-not-empty');
            }

            this.setState({
                filterList: [],
                filterValue: ''
            });

            this.props.clearFilterFalse();
        }

        if (this.props.executeSuccess) {
            if (this.refs['re-execute'] && this.refs['re-execute'].refs['buttonEl'].classList.contains('clicked')) {
                this.refs['re-execute'].refs['buttonEl'].classList.remove('clicked');
                this.state.checkedList = [];
            }
        }

        if (this.props.executeError) {
            if (this.refs['re-execute'] && this.refs['re-execute'].refs['buttonEl'].classList.contains('clicked')) {
                this.refs['re-execute'].refs['buttonEl'].classList.remove('clicked');
                this.refs['re-execute'].refs['buttonEl'].disabled = false;
            }
        }

        if (this.props.clearCheckedList) {
            this.state.checkedList = [];
            this.props.clearCheckedListFalse();
        }
    }

    filter(value) {
        let filterResults = this.props.jobResults.filter(item => {
            return item.minionName.toLowerCase().search(value.toLowerCase()) !== -1 || item.minionGroups.toLowerCase().search(value.toLowerCase()) !== -1;
        });

        let newCheckedList = [];

        filterResults.map(item => {
            this.state.checkedList.map(el => {
                if (item.id === el) {
                    if (this.refs[el].refs['inputEl'].checked) {
                        newCheckedList.push(el);
                    }
                }
            })
        });

        this.setState({
            filterList: filterResults,
            filterValue: value.length,
            checkedList: newCheckedList
        })
    }

    showJobDetails(result) {
        this.props.showJobDetails(result);
    }

    addToRestartList(e, id) {
        if (e.target.tagName === 'INPUT') {
            if (e.target.checked) {
                this.state.checkedList.push(id);
            } else {
                let index = this.state.checkedList.indexOf(id);
                this.state.checkedList.splice(index, 1);
                if (this.refs['table'].getElementsByClassName('header-checkbox')[0].getElementsByTagName('input')[0].checked) {
                    this.refs['table'].getElementsByClassName('header-checkbox')[0].getElementsByTagName('input')[0].checked = false;
                }
            }

            this.setState({clicked: true});
        }
    }

    addAll(e) {
        if (e.target.tagName === 'INPUT') {

            let jobResults = this.state.filterValue ? this.state.filterList : this.props.jobResults,
                checked = e.target.checked,
                inputs = this.refs['table'].getElementsByTagName('input');

            this.state.checkedList = [];

            if (checked) {
                for (let i = 0; i < jobResults.length; i++) {
                    if (!jobResults[i].reExecuted) {
                        this.state.checkedList.push(jobResults[i].id);
                    }
                }
            }

            for (let i = 0; i < inputs.length; i++) {
                if (inputs[i].type && inputs[i].type === 'checkbox') {
                    inputs[i].checked = checked;
                }
            }
        }

        this.setState({clicked: true});
    }

    reExecuteScripts() {

        let button = this.refs['re-execute'].refs['buttonEl'];

        if (!button.classList.contains('clicked')) {
            button.classList.add('clicked');
            button.disabled = true;
            this.props.reExecuteScripts(this.state.checkedList);
            this.props.setExecuteFalse();
            this.props.setExecuteErrorFalse();
        }
    }

    render() {

        let jobResults = this.state.filterValue ? this.state.filterList : this.props.jobResults,
            showHeadCheckBox = true,
            reExecutedMinionsLength = 0,
            messages = this.props.messages;

        if (jobResults.length) {
            jobResults.map((el) => {
                if (!el.reExecuted) {
                    showHeadCheckBox = false;
                }
            });
        }

        if (this.state.filterValue) {
            showHeadCheckBox = true
        }

        let template = <div className='block-list block-list__table'>
            <table width='100%' className='mui-table' ref='table'>
                <tbody>
                <tr>
                    <td className='table__head'>{messages['client.jobresults.table.minion']}</td>
                    <td className='table__head'>{messages['client.jobresults.table.group']}</td>
                    <td className='table__head'>{messages['client.jobresults.table.date.update']}</td>
                    {this.props.showStatus ? <td className='table__head'>{messages['client.jobresults.table.status']}</td> : null}
                    {this.props.showSelect ? <td className='table__head'>
                            <span className='mui--pull-left'>{messages['client.jobresults.table.select']}</span>
                            {!showHeadCheckBox ?
                                <Checkbox className='mui--pull-right header-checkbox' title={messages['client.jobresults.table.select.all']}
                                          ref='header-checkbox'
                                          onClick={e => {
                                              ::this.addAll(e);
                                          }}/> : null}

                        </td> : null}
                </tr>
                {jobResults.length ? jobResults.map((item, index) => {
                        if (item.reExecuted) reExecutedMinionsLength++;
                        return <tr key={index} ref={index}>
                            <td>{item.minionName}
                            <a className='table__link mui--text-right' onClick={() => {
                                ::this.showJobDetails(item);
                            }}> (log)</a></td>
                            <td>{item.minionGroups}</td>
                            <td>{item.lastModifiedDate}</td>
                            {this.props.showStatus ? <td>{item.status}</td> : null}
                            {this.props.showSelect ?
                                <td>{item.reExecuted ? messages['client.jobresults.table.reexecuted'] : <Checkbox ref={item.id} onClick={e => {
                                        ::this.addToRestartList(e, item.id);
                                    }}/>}
                                </td>
                                : null}
                        </tr>;
                    }) : null}
                </tbody>
            </table>
            {this.props.executeError ? <span className='input_error'>{messages['client.error.jobresults.reexecute']}</span> : null}
            {this.props.executeSuccess ? <span className='success-mess'>{messages['client.jobresults.reexecute.success']}</span> : null}
            {this.props.showSelect && reExecutedMinionsLength != jobResults.length ?
                <Button size='small' color='primary' variant='flat' className='modal__btn mui--pull-right'
                        ref='re-execute'
                        disabled={!this.state.checkedList.length}
                        onClick={::this.reExecuteScripts}>
                    {messages['client.btn.reexecute']}
                </Button>
                : null}
        </div>;

        return jobResults.length || this.state.filterValue ? <div className='right-block-list'>
                <Input label={messages['client.input.search']} floatingLabel={true} id='filter' onChange={e => {
                    ::this.filter(e.target.value);
                }}/>
                {template}
            </div> : <div className='right-block-list'>{messages['no.data']}</div>
    }
}