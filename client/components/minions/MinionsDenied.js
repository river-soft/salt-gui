import React, {Component} from 'react';
import Input from 'muicss/lib/react/input';

export default class MinionsDenied extends Component {

    constructor(props) {
        super(props);

        this.state = {
            filterList: [],
            rerender: false
        }
    }

    filter(value) {
        let minions = this.props.deniedMinions.filter(item => {
            return item.toLowerCase().search(value.toLowerCase()) !== -1;
        });

        this.setState({
            filterList: minions,
            rerender: value.length
        })
    }


    render() {

        let deniedMinions = this.state.rerender ? this.state.filterList : this.props.deniedMinions;

        return <div className='right-block-list'>
            <Input label='Поиск' floatingLabel={true} onChange={e => {::this.filter(e.target.value)}} />
            <div className='block-list block-list__table'>
                <table width='100%' className='mui-table'>
                    <tbody>
                    <tr>
                        <td className='table__head'>NAME</td>
                    </tr>
                    {deniedMinions ? deniedMinions.map((item, index) => {
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