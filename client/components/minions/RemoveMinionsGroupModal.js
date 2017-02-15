import React, {Component} from 'react';
import Button from 'muicss/lib/react/button';
import Divider from 'muicss/lib/react/divider';

export default class RemoveMinionsGroupModal extends Component {

    render() {

        return <div className='modal__content'>
            <div className='modal__close_btn' onClick={this.props.closeModal}>X</div>
            <div className='modal__header mui--text-center'>
                Удаление группы
            </div>
            <div className='modal__content_question mui--text-center'>
                Вы действительно хотите удалить группу {this.props.group.name}
                {this.props.group.size ? ', которая содержит ' + this.props.group.size + ' миньонов' : null}?
            </div>
            {this.props.error ? <span className='input_error'>{this.props.error.message}</span> : null}
            <div className='modal__footer'>
                <Divider/>
                <Button size='small' color='primary' variant='flat' className='modal__btn mui--pull-right'
                        onClick={() => {
                            this.props.removeGroup(this.props.group.id)
                        }}>Удалить</Button>
                <Button size='small' color='primary' variant='flat' onClick={this.props.closeModal}
                        className='modal__btn mui--pull-right'>Отменить</Button>
            </div>
        </div>
    }
}