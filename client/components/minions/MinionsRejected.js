import React, {Component} from 'react';
import Input from 'muicss/lib/react/input';

export default class MinionsRejected extends Component {

    constructor(props) {
        super(props);

        this.state = {
            filterList: [],
            rerender: false
        }
    }

    filter(value) {
        let minions = this.props.rejectedMinions.filter(item => {
            return item.toLowerCase().search(value.toLowerCase()) !== -1;
        });

        this.setState({
            filterList: minions,
            rerender: value.length
        })
    }

    render() {

        let rejectedMinions = this.state.rerender ? this.state.filterList : this.props.rejectedMinions;

        return <div className='right-block-list'>
            <Input label='Поиск' floatingLabel={true} onChange={e => {::this.filter(e.target.value)}} />
            <div className='block-list block-list__table'>
                <table width='100%' className='mui-table'>
                    <tbody>
                    <tr>
                        <td className='table__head'>NAME</td>
                    </tr>
                    {rejectedMinions ? rejectedMinions.map((item, index) => {
                            return <tr key={index}>
                                <td>{item}</td>
                            </tr>
                        }) : <tr><td>Данных нет</td></tr>}
                    </tbody>
                </table>
            </div>
        </div>
    }
}