import React, {Component} from 'react';
import Input from 'muicss/lib/react/input';
import Divider from 'muicss/lib/react/divider';
import Button from 'muicss/lib/react/button';

export default class CreateMinionsGroupModal extends Component {

    constructor(props) {
        super(props);

        this.state = {
            groupName: '',
            groupExist: false
        }
    }

    addGroupName(name) {
        this.setState({
            groupName: name,
            groupExist: false
        });
    }

    checkGroupName(name) {
        this.props.groups.map(group => {
            if(group.group === name) {
                this.setState({groupExist: true});
            }
        })
    }

    createGroup() {
        this.props.createMinionsGroup(this.state.groupName);
    }

    render() {

        let messages = this.props.messages;

        return <div className='modal__content'>
            <div className='modal__close_btn' onClick={this.props.closeModal}>X</div>
            <div className='modal__header mui--text-center'>
                {messages['client.modal.minions.create.group.title']}
            </div>
            <Input label={messages['client.input.group.name']} floatingLabel={true} onChange={(e) => {
                this.addGroupName(e.target.value);
                this.checkGroupName(e.target.value);
            }}/>
            {this.state.groupExist ? <span className='input_error'>{messages['client.error.group.exists']}</span> : null}
            <div className='modal__footer'>
                <Divider/>
                <Button size='small' color='primary' variant='flat' className='modal__btn mui--pull-right'
                        onClick={::this.createGroup} disabled={this.state.groupName === '' || this.state.groupExist}>{messages['client.btn.save']}</Button>
            </div>
        </div>
    }
}