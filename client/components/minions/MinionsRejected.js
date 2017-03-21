import React, {Component} from 'react';
import Input from 'muicss/lib/react/input';
import Button from 'muicss/lib/react/button';
import Checkbox from 'muicss/lib/react/checkbox';
import Divider from 'muicss/lib/react/divider';
import Modal from 'react-modal';

export default class MinionsRejected extends Component {

    constructor(props) {
        super(props);

        this.state = {
            filterList: [],
            rerender: false,
            checkedList: [],
            clicked: false,
            showModal: false,
            delete: false
        }
    }

    componentDidUpdate() {
        if (this.props.deleteMinionsSuccess && this.state.showModal) {

            this.state = {checkedList: []};

            this.props.setDeletedFalse();
            this.onRequestClose();

            let inputs = document.getElementsByTagName('input');
            for (let i = 0; i < inputs.length; i++) {
                inputs[i].checked = false
            }
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

    addToCheckedList(e, minion) {
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

    deleteMinions() {
        this.setState({
            delete: true,
            showModal: true
        })
    }

    onRequestClose() {
        this.setState({
            showModal: false
        });
    }

    sendDeleteMinions() {
        this.props.deleteMinions(this.state.checkedList)
    }

    render() {

        let rejectedMinions = this.state.rerender ? this.state.filterList : this.props.rejectedMinions,
            modal;

        if (this.state.delete) {
            modal = <div className='modal__content'>
                <div className='modal__close_btn' onClick={::this.onRequestClose}>X</div>
                <h4 className='mui--text-center modal__header'>Удалить миньон</h4>
                <div className='modal__body'>
                    Вы уверены что хотите удалить миньон:  {this.state.checkedList.map((item) => {
                    return item + ' '
                })}
                </div>
                <div className='modal__footer'>
                    <Divider />
                    <Button size='small' color='primary' variant='flat' onClick={::this.sendDeleteMinions}
                            className='modal__btn mui--pull-right'>Удалить</Button>
                </div>
            </div>
        }


        return <div className='right-block-list'>
            <Input label='Поиск' floatingLabel={true} onChange={e => {
                ::this.filter(e.target.value)
            }}/>
            <div className='block-list block-list__table'>
                <table width='100%' className='mui-table'>
                    <tbody>
                    <tr>
                        <td className='table__head'>Название</td>
                        <td className='table__head'>Выбор</td>
                    </tr>
                    {rejectedMinions ? rejectedMinions.map((item, index) => {
                            return <tr key={index}>
                                <td>{item}</td>
                                <td><Checkbox
                                    onClick={e => {
                                        ::this.addToCheckedList(e, item)
                                    }}
                                /></td>
                            </tr>
                        }) : <tr>
                            <td colSpan='2'>Данных нет</td>
                        </tr>}
                    </tbody>
                </table>
                {rejectedMinions.length ?
                    <Button size='small' color='primary' variant='flat' className='modal__btn mui--pull-right'
                            disabled={!this.state.checkedList.length} onClick={::this.deleteMinions}>
                        Удалить
                    </Button> : null}
            </div>
            <Modal contentLabel='label' isOpen={this.state.showModal} className='modal'
                   onRequestClose={::this.onRequestClose} overlayClassName='overlay'
                   parentSelector={() => document.body} ariaHideApp={false}>
                {modal}
            </Modal>
        </div>
    }
}