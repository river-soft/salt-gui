import React, {Component} from 'react';
import Input from 'muicss/lib/react/input';
import Checkbox from 'muicss/lib/react/checkbox';
import Button from 'muicss/lib/react/button';

export default class MinionsUnaccepted extends Component {

    constructor(props) {
        super(props);

        this.state = {
            filterList: [],
            rerender: false,
            acceptingList: []
        }
    }

    filter(value) {
        let minions = this.props.unacceptedMinions.filter(item => {
            return item.toLowerCase().search(value.toLowerCase()) !== -1;
        });

        this.setState({
            filterList: minions,
            rerender: value.length
        })
    }

    addToAcceptingList(e, minion) {
        if(e.target.checked) {
            this.state.acceptingList.push(minion);
        } else {
            let index = this.state.acceptingList.indexOf(minion);
            delete this.state.acceptingList[index];
        }
    }

    render() {

        let unacceptedMinions = this.state.rerender ? this.state.filterList : this.props.unacceptedMinions;

        return <div className='right-block-list'>
            <Input label='Поиск' floatingLabel={true} onChange={e => {::this.filter(e.target.value)}} />
            <div className='block-list block-list__table'>
                <table width='100%' className='mui-table'>
                    <tbody>
                    <tr>
                        <td className='table__head'>NAME</td>
                        <td className='table__head'>SELECT</td>
                    </tr>
                    {unacceptedMinions ? unacceptedMinions.map((item, index) => {
                            return <tr key={index}>
                                <td>{item}</td>
                                <td><Checkbox onClick={e => {::this.addToAcceptingList(e, item)}} /></td>
                            </tr>
                        }) : <tr><td>Данных нет</td></tr>}
                    </tbody>
                </table>
                <Button size='small' color='primary' variant='flat' className='modal__btn mui--pull-right'>
                    reject
                </Button>
                <Button size='small' color='primary' variant='flat' className='modal__btn mui--pull-right'>
                    DELETE
                </Button>
                <Button size='small' color='primary' variant='flat' className='modal__btn mui--pull-right'>
                    Accept
                </Button>
            </div>
        </div>
    }
}