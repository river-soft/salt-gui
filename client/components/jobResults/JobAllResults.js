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
            checkedList: [],
            restarted: false
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
    }

    filter(value) {
        let filterResults = this.props.jobResults.filter(item => {
            return item.minionName.toLowerCase().search(value.toLowerCase()) !== -1;
        });

        this.setState({
            filterList: filterResults,
            filterValue: value.length
        })
    }

    showJobDetails(result) {
        this.props.showJobDetails(result);
    }

    addToRestartList(e, minion) {
        if (e.target.tagName === 'INPUT') {
            if (e.target.checked) {
                this.state.checkedList.push(minion);
            } else {
                let index = this.state.checkedList.indexOf(minion);
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

            for (let i = 0; i < jobResults.length; i++) {
                this.state.checkedList.push(jobResults[i].minionName);
            }

            for (let i = 0; i < inputs.length; i++) {
                if (inputs[i].type && inputs[i].type === 'checkbox') {
                    inputs[i].checked = checked;
                }
            }
        }

        this.setState({clicked: true});
    }

    executeScripts() {
        this.props.executeScripts(this.state.checkedList, this.props.scriptName);
        this.setState({restarted: true});
    }

    render() {

        let jobResults = this.state.filterValue ? this.state.filterList : this.props.jobResults,
            template = <div className='block-list block-list__table'>
                <table width='100%' className='mui-table' ref='table'>
                    <tbody>
                    <tr>
                        <td className='table__head'>Minion</td>
                        <td className='table__head'>Group</td>
                        <td className='table__head'>Latest Report</td>
                        {this.props.showStatus ? <td className='table__head'>Status</td> : null}
                        {this.props.showSelect ? <td className='table__head'>
                                <span className='mui--pull-left'>Select</span>
                                <Checkbox className='mui--pull-right header-checkbox' title='Выбрать все'
                                          ref='header-checkbox'
                                          onClick={e => {
                                              ::this.addAll(e);
                                          }}/>
                            </td> : null}
                    </tr>
                    {jobResults.length ? jobResults.map((item, index) => {
                            return <tr key={index} ref={index}>
                                <td><a className='table__link' onClick={() => {
                                    ::this.showJobDetails(item);
                                }}>{item.minionName}</a></td>
                                <td>{item.minionGroups}</td>
                                <td>{item.lastModifiedDate}</td>
                                {this.props.showStatus ? <td>{item.status}</td> : null}
                                {this.props.showSelect ?
                                    <td><Checkbox onClick={e => {
                                        ::this.addToRestartList(e, item.minionName)
                                    }}/></td>
                                    : null}
                            </tr>;
                        }) : null}
                    </tbody>
                </table>
                {this.props.showSelect ?
                    <Button size='small' color='primary' variant='flat' className='modal__btn mui--pull-right'
                            disabled={!this.state.checkedList.length || this.state.restarted} onClick={() => {
                        ::this.executeScripts();
                    }}>
                        Restart
                    </Button>
                    : null}
            </div>;

        return jobResults.length || this.state.filterValue ? <div className='right-block-list'>
                <Input label='Поиск' floatingLabel={true} id='filter' onChange={e => {
                    ::this.filter(e.target.value);
                }}/>
                {template}
            </div> : <div className='right-block-list'>No results</div>
    }
}