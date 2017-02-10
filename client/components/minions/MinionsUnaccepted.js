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
            delete: false,
            reject: false,
            clicked: false,
            closeModal: false
        }
    }

    componentDidUpdate() {
        if(this.props.acceptMinionsSuccess.length && this.state.showModal) {
            this.state = {
                checkedList: [],
                groupsList: [],
            };

            this.props.acceptMinionsSuccess.splice(0, this.props.acceptMinionsSuccess.length);

            this.onRequestClose();

            let inputs = document.getElementsByTagName('input');
            for(let i = 0; i < inputs.length; i++) {
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
        if (e.target.checked) {
            this.state.checkedList.push(minion);
        } else {
            let index = this.state.checkedList.indexOf(minion);
            this.state.checkedList.splice(index, 1);
        }

        this.setState({clicked: true});
    }

    addToGroupsList(e, group) {
        if(e.target.checked) {
            this.state.groupsList.push(group);
        } else {
            let index = this.state.groupsList.indexOf(group);
            this.state.groupsList.splice(index, 1);
        }

        this.setState({clicked: true});
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
            delete: false,
            reject: false
        })
    }

    sendAcceptedMinionsAndGroups() {
        this.props.acceptMinions(this.state.checkedList, this.state.groupsList);
    }

    render() {

        const getMinionsGroups = this.props.getMinionsGroups;

        let unacceptedMinions = this.state.rerender ? this.state.filterList : this.props.unacceptedMinions,
            minionsGroups = this.props.minionsGroups, modal;

        if (this.state.accept) {
            modal = <div className='modal__content'>
                <div className='modal__close_btn' onClick={::this.onRequestClose}>X</div>
                <h4 className='mui--text-center modal__header'>Accept minions</h4>
                <div className='modal__body'>
                    <div className='block-list'>
                        <h6 className='block-list__header'>Choose groups</h6>
                        {minionsGroups.length ? minionsGroups.map((item, index) => {
                                return <Checkbox label={item.name} key={index} onClick={e => {
                                    ::this.addToGroupsList(e, item.name)}}
                                />
                            }) : <span>Групп нет</span>}
                    </div>
                </div>
                <div className='modal__footer'>
                    <Divider />
                    <Button size='small' color='primary' variant='flat' onClick={::this.sendAcceptedMinionsAndGroups}
                            className='modal__btn mui--pull-right' disabled={!this.state.groupsList.length}>Accept</Button>
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
                        <td className='table__head'>NAME</td>
                        <td className='table__head'>SELECT</td>
                    </tr>
                    {unacceptedMinions ? unacceptedMinions.map((item, index) => {
                            return <tr key={index}>
                                <td>{item}</td>
                                <td><Checkbox onClick={e => {
                                    ::this.addToAcceptingList(e, item)
                                }}/></td>
                            </tr>
                        }) : <tr>
                            <td>Данных нет</td>
                        </tr>}
                    </tbody>
                </table>
                <Button size='small' color='primary' variant='flat' className='modal__btn mui--pull-right' disabled={!this.state.checkedList.length}>
                    reject
                </Button>
                <Button size='small' color='primary' variant='flat' className='modal__btn mui--pull-right' disabled={!this.state.checkedList.length}>
                    delete
                </Button>
                <Button size='small' color='primary' variant='flat' className='modal__btn mui--pull-right' disabled={!this.state.checkedList.length} onClick={
                    () => {
                        getMinionsGroups();
                        this.acceptMinions();
                    }}>
                    accept
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