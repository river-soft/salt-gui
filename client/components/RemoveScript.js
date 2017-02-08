import React, {Component} from 'react';
import Button from 'muicss/lib/react/button';
import Divider from 'muicss/lib/react/divider';

export default class RemoveScript extends Component {

    constructor(props) {
        super(props);

        this.state = {
            showModal: false
        };

        this.removeScript = this.removeScript.bind(this);
    }

    componentDidUpdate() {
        if (this.props.removeSuccess && !this.state.showModal) {
            this.setState({
                closeModal: true
            });

            this.props.closeModal();
            this.props.hideContent();
        }
    }

    removeScript(script) {
        this.props.scriptRemove(script);
    }

    render() {

        return <div className='modal__content'>
            <div className='modal__close_btn' onClick={this.props.closeModal}>X</div>
            <div className='modal__content_question mui--text-center'>
                Вы действительно хотите удалить файл {this.props.script.name}?
            </div>
            <div className='modal__footer'>
                <Divider/>
                <Button size='small' color='primary' variant='flat' onClick={() => {
                    this.removeScript(this.props.script);
                }} className='modal__btn mui--pull-right'>Удалить</Button>
                <Button size='small' color='primary' variant='flat' onClick={this.props.closeModal}
                        className='modal__btn mui--pull-right'>Отменить</Button>
            </div>
        </div>
    }
}