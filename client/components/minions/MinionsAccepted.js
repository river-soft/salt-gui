import React, {Component} from 'react';
import Input from 'muicss/lib/react/input';
import Checkbox from 'muicss/lib/react/checkbox';
import Button from 'muicss/lib/react/button';
import Divider from 'muicss/lib/react/divider';
import Modal from 'react-modal';

export default class MinionsAccepted extends Component {

    constructor(props) {
        super(props);

        this.state = {
            filterList: [],
            rerender: false,
            checkedList: [],
            showModal: false,
            delete: false
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

    addToCheckedList(e, minion) {
        if (e.target.tagName === 'INPUT') {
            if (e.target.checked) {
                this.state.checkedList.push(minion.name);
            } else {
                let index = this.state.checkedList.indexOf(minion.name);
                this.state.checkedList.splice(index, 1);
            }

            this.setState({clicked: true});
        }
    }

    deleteMinions() {
        this.setState({
            delete: true,
            showModal: true
        });
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

        let acceptedMinions = this.state.rerender ? this.state.filterList : this.props.acceptedMinions,
            modal;

        if (this.state.delete) {
            modal = <div className='modal__content'>
                <div className='modal__close_btn' onClick={::this.onRequestClose}>X</div>
                <h4 className='mui--text-center modal__header'>Удалить миньон</h4>
                <div className='modal__body'>
                    Вы уверены что хотите удалить миньон: {this.state.checkedList.map((item) => {
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
                        <td className='table__head'>Группа</td>
                        <td className='table__head'>Выбор</td>
                    </tr>
                    {acceptedMinions ? acceptedMinions.map((item, index) => {
                            return <tr key={index}>
                                <td>{item.name}</td>
                                <td>{item.groups}</td>
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
                {acceptedMinions.length ?
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