import React, {Component} from 'react';
import Button from 'muicss/lib/react/button';
import Divider from 'muicss/lib/react/divider';

export default class RemoveMinionsGroupModal extends Component {

    render() {

        let messages = this.props.messages;

        return <div className='modal__content'>
            <div className='modal__close_btn' onClick={this.props.closeModal}>X</div>
            <div className='modal__header mui--text-center'>
                {messages['client.modal.group.delete']}
            </div>
            <div className='modal__content_question mui--text-center'>
                {messages['client.modal.group.confirm.delete']} {this.props.group.name}
                {this.props.group.size ? ', ' + messages['client.modal.group.confirm.contains'] + ' ' + this.props.group.size + ' ' + messages['client.modal.group.confirm.minions'] : null}?
            </div>
            {this.props.error ? <span className='input_error'>{this.props.error.message}</span> : null}
            <div className='modal__footer'>
                <Divider/>
                <Button size='small' color='primary' variant='flat' className='modal__btn mui--pull-right'
                        onClick={() => {
                            this.props.removeGroup(this.props.group.id)
                        }}>{messages['client.btn.delete']}</Button>
            </div>
        </div>
    }
}