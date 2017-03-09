import React, {Component} from 'react';
import Input from 'muicss/lib/react/input';

export default class JobAllResults extends Component {

    constructor(props) {
        super(props);

        this.state = {
            filterList: [],
            filterValue: ''
        };
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

                    </tr>
                    {jobResults.length ? jobResults.map((item, index) => {
                        return <tr key={index}>
                            <td>{item.minionName}</td>
                            <td>{item.minionGroups}</td>
                            <td>{item.lastModifiedDate}</td>
                            {this.props.showStatus ? <td>{item.status}</td> : null}
                        </tr>
                    }) : null}
                    </tbody>
                </table>
            </div>;

        return jobResults.length || this.state.filterValue ? <div className='right-block-list'>
                <Input label='Поиск' floatingLabel={true} onChange={e => {
                    ::this.filter(e.target.value);
                }}/>
                {template}
            </div> : <div className='right-block-list'>No results</div>
    }
}