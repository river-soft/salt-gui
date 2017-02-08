import React, {Component} from 'react';
import Form from 'muicss/lib/react/form';
import Input from 'muicss/lib/react/input';
import TextArea from 'muicss/lib/react/textarea';
import Divider from 'muicss/lib/react/divider';
import Button from 'muicss/lib/react/button';

export default class EditScript extends Component {

    render() {

        return <div className='modal__content'>
            <div className='modal__close_btn' onClick={this.props.closeModal}>X</div>
            <h4 className='mui--text-center modal__header'>Редактирование скрипта</h4>
            <Form className='modal__form'>
                <div className='modal__form_group'>
                    <Input label='Название группы' floatingLabel={true} name='group' id='group'/>
                    <Input label='Название скрипта' floatingLabel={true} defaultValue={this.props.script.name || ''}/>
                    <TextArea label='Текст скрипта' floatingLabel={true} className='modal__textarea' defaultValue={this.props.script.content || ''}/>
                </div>
            </Form>
            <div className='modal__footer'>
                <Divider />
                <Button size='small' color='primary' variant='flat'
                        className='modal__btn mui--pull-right'>Сохранить</Button>
            </div>
        </div>
    }
}