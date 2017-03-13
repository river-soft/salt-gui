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
            }

            this.setState({clicked: true});
        }
    }

    render() {

        let jobResults = this.state.filterValue ? this.state.filterList : this.props.jobResults,
            template = <div className='block-list block-list__table'>
                <table width='100%' className='mui-table'>
                    <tbody>
                    <tr>
                        <td className='table__head'>Minion</td>
                        <td className='table__head'>Group</td>
                        <td className='table__head'>Latest Report</td>
                        {this.props.showStatus ? <td className='table__head'>Status</td> : null}
                        {this.props.showSelect ? <td className='table__head'>Select</td> : null}
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
                                    <td><Checkbox onClick={(e) => {
                                        ::this.addToRestartList(e, item.minionName)
                                    }}/></td>
                                    : null}
                            </tr>;
                        }) : null}
                    </tbody>
                </table>
                {this.props.showSelect ?
                    <Button size='small' color='primary' variant='flat' className='modal__btn mui--pull-right'
                            disabled={!this.state.checkedList.length} onClick={() => {
                        console.log('CLICK')
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