import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import Form from 'muicss/lib/react/form';
import Input from 'muicss/lib/react/input';
import Button from 'muicss/lib/react/button';
import * as authorization from '../actions/AuthorizationAction';

class Authorization extends Component {

    constructor(props) {
        super(props);

        this.state = {
            userName: '',
            password: ''
        }
    }

    setUserName(e) {
        this.state.userName = e.target.value;
    }

    setPassword(e) {
        this.state.password = e.target.value;
    }

    login(e) {
        e.preventDefault();
        const {authorization} = this.props.authorization;

        authorization(this.state.userName, this.state.password);
    }

    render() {

        return <Form>
            <legend>Авторизация</legend>
            <Input label='Логин' required={true} onChange={::this.setUserName}/>
            <Input label='Пароль' type='password' floatingLabel={true} required={true} onChange={::this.setPassword}/>
            <Button variant='raised' onClick={::this.login}>Авторизироваться</Button>
        </Form>
    }
}

function mapStateToProps(state) {
    return {
        auth: state.auth
    }
}

function mapDispatchToProps(dispatch) {
    return {
        authorization: bindActionCreators(authorization, dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(Authorization)