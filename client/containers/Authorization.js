import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import Form from 'muicss/lib/react/form';
import Input from 'muicss/lib/react/input';
import * as authorization from '../actions/AuthorizationAction';
import {Header} from '../components/Header';
import Container from 'muicss/lib/react/container';
import Col from 'muicss/lib/react/col';
import Row from 'muicss/lib/react/row';

class Authorization extends Component {

    constructor(props) {
        super(props);

        this.state = {
            userName: '',
            password: ''
        }
    }

    setUserName(e) {
        this.props.auth.error = '';
        this.setState({
            userName: e.target.value
        });
    }

    setPassword(e) {
        this.props.auth.error = '';
        this.setState({
            password: e.target.value
        });
    }

    login(e) {
        e.preventDefault();

        const {authorization} = this.props.authorization;

        authorization(this.state.userName, this.state.password);
    }

    render() {

        return <div className='wrapper'>
            <Header/>
            <main className='main'>
                <Container>
                    <Row>
                        <Col md='4' md-offset='4' xs='12' lg='4' lg-offset='4'>
                            <Form className='authorization'>
                                <legend className='authorization__header'>
                                    <p>
                                        Авторизация
                                    </p>
                                </legend>
                                <div className='authorization__content'>
                                    <Input label='Логин' onChange={::this.setUserName} floatingLabel={true}/>
                                    <Input label='Пароль' type='password' floatingLabel={true}
                                           onChange={::this.setPassword}/>
                                    {this.props.auth.error ? <span className='input_error'>Вы ввели не верный логин или пароль</span> : null}
                                    <button className='mui-btn button authorization__btn' onClick={::this.login}
                                            disabled={!this.state.userName.length || !this.state.password}>Авторизироваться</button>
                                </div>
                            </Form>
                        </Col>
                    </Row>
                </Container>
            </main>
        </div>
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