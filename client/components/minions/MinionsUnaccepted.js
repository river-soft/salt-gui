import React, {Component} from 'react';
import Input from 'muicss/lib/react/input';
import Checkbox from 'muicss/lib/react/checkbox';
import Button from 'muicss/lib/react/button';
import Divider from 'muicss/lib/react/divider';
import Modal from 'react-modal';

export default class MinionsUnaccepted extends Component {

    constructor(props) {
        super(props);

        this.state = {
            filterList: [],
            rerender: false,
            checkedList: [],
            groupsList: [],
            showModal: false,
            accept: false,
            reject: false,
            clicked: false,
            closeModal: false
        }
    }

    componentDidUpdate() {
        if (this.props.acceptMinionsSuccess.length && this.state.showModal) {
            this.state = {
                checkedList: [],
                groupsList: [],
            };

            this.props.acceptMinionsSuccess.splice(0, this.props.acceptMinionsSuccess.length);

            this.onRequestClose();

            let inputs = document.getElementsByTagName('input');
            for (let i = 0; i < inputs.length; i++) {
                inputs[i].checked = false
            }
        }

        if (this.props.rejectMinionsSuccess && this.state.showModal) {
            this.state = {
                checkedList: [],
                groupsList: [],
            };

            this.props.setRejectedFalse();
            this.onRequestClose();

            let inputs = document.getElementsByTagName('input');
            for (let i = 0; i < inputs.length; i++) {
                inputs[i].checked = false
            }
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

    addToGroupsList(e, group) {
        if (e.target.tagName === 'INPUT') {
            if (e.target.checked) {
                this.state.groupsList.push(group);
            } else {
                let index = this.state.groupsList.indexOf(group);
                this.state.groupsList.splice(index, 1);
            }

            this.setState({clicked: true});
        }
    }

    onRequestClose() {
        this.setState({
            groupsList: [],
            showModal: false
        });
    }

    acceptMinions() {
        this.setState({
            showModal: true,
            accept: true,
            reject: false
        })
    }

    rejectMinions() {
        this.setState({
            showModal: true,
            accept: false,
            delete: false,
            reject: true
        })
    }

    sendAcceptedMinionsAndGroups() {
        this.props.acceptMinions(this.state.checkedList, this.state.groupsList);
    }

    sendRejectedMinions() {
        this.props.rejectMinions(this.state.checkedList);
    }

    render() {

        const getMinionsGroups = this.props.getMinionsGroups;

        let unacceptedMinions = this.state.rerender ? this.state.filterList : this.props.unacceptedMinions,
            minionsGroups = this.props.minionsGroups, modal;

        if (this.state.accept) {
            modal = <div className='modal__content'>
                <div className='modal__close_btn' onClick={::this.onRequestClose}>X</div>
                <h4 className='mui--text-center modal__header'>Принять миньон</h4>
                <div className='modal__body'>
                    <div className='block-list'>
                        <h6 className='block-list__header'>Выберите группы</h6>
                        {minionsGroups.length ? minionsGroups.map((item, index) => {
                                return <Checkbox label={item.name} key={index} onClick={e => {
                                    ::this.addToGroupsList(e, item.name)
                                }}
                                />
                            }) : <span>Групп нет</span>}
                    </div>
                </div>
                <div className='modal__footer'>
                    <Divider />
                    <Button size='small' color='primary' variant='flat' onClick={::this.sendAcceptedMinionsAndGroups}
                            className='modal__btn mui--pull-right'
                            disabled={!this.state.groupsList.length}>Принять</Button>
                </div>
            </div>
        } else if (this.state.reject) {
            modal = <div className='modal__content'>
                <div className='modal__close_btn' onClick={::this.onRequestClose}>X</div>
                <h4 className='mui--text-center modal__header'>Отклонить миньон</h4>
                <div className='modal__body'>
                    Вы уверены что хотите отклонить миньон: {this.state.checkedList.map((item) => {
                    return item + ' '
                })}
                </div>
                <div className='modal__footer'>
                    <Divider />
                    <Button size='small' color='primary' variant='flat' onClick={::this.sendRejectedMinions}
                            className='modal__btn mui--pull-right'>Отклонить</Button>
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
                    {unacceptedMinions ? unacceptedMinions.map((item, index) => {
                            return <tr key={index}>
                                <td>{item}</td>
                                <td><Checkbox onClick={e => {
                                    ::this.addToAcceptingList(e, item)
                                }}/></td>
                            </tr>
                        }) : <tr>
                            <td colSpan='2'>Данных нет</td>
                        </tr>}
                    </tbody>
                </table>
                <Button size='small' color='primary' variant='flat' className='modal__btn mui--pull-right'
                        disabled={!this.state.checkedList.length} onClick={::this.rejectMinions}>
                    Отклонить
                </Button>
                <Button size='small' color='primary' variant='flat' className='modal__btn mui--pull-right'
                        disabled={!this.state.checkedList.length} onClick={
                    () => {
                        getMinionsGroups();
                        this.acceptMinions();
                    }}>
                    Принять
                </Button>
            </div>
            <Modal contentLabel='label' isOpen={this.state.showModal} className='modal'
                   onRequestClose={::this.onRequestClose} overlayClassName='overlay'
                   parentSelector={() => document.body} ariaHideApp={false}>
                {modal}
            </Modal>
        </div>
    }
}