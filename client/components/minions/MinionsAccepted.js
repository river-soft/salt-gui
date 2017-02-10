import React, {Component} from 'react';
import Input from 'muicss/lib/react/input';

export default class MinionsAccepted extends Component {

    constructor(props) {
        super(props);

        this.state = {
            filterList: [],
            rerender: false
        }
    }

    filter(value) {
        let minions = this.props.acceptedMinions.filter(item => {
            return item.name.toLowerCase().search(value.toLowerCase()) !== -1;
        });

        this.setState({
            filterList: minions,
            rerender: value.length
        })
    }

    render() {

        let acceptedMinions = this.state.rerender ? this.state.filterList : this.props.acceptedMinions;

        return <div className='right-block-list'>
            <Input label='Поиск' floatingLabel={true} onChange={e => {::this.filter(e.target.value)}} />
            <div className='block-list block-list__table'>
                <table width='100%' className='mui-table'>
                    <tbody>
                    <tr>
                        <td className='table__head'>NAME</td>
                        <td className='table__head'>GROUP</td>
                    </tr>
                    {acceptedMinions ? acceptedMinions.map((item, index) => {
                            return <tr key={index}>
                                <td>{item.name}</td>
                                <td>{item.groups}</td>
                            </tr>
                        }) : <tr><td colSpan='2'>Данных нет</td></tr>}
                    </tbody>
                </table>
            </div>
        </div>
    }
}