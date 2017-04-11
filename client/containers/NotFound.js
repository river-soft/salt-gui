import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import {Header} from '../components/Header';
import Container from 'muicss/lib/react/container';
import Col from 'muicss/lib/react/col';
import Row from 'muicss/lib/react/row';
import NotFoundImg from '../styles/images/not-found.png';
import * as getMessagesAction from '../actions/GetMessagesAction';

class NotFound extends Component {

    constructor(props) {
        super(props);

        this.state = {
            strings: ''
        }
    }

    componentWillMount() {
        if(!this.props.localization) {
            const {getMessages} = this.props.getMessagesAction;

            getMessages();
        } else {
            this.setState({strings : this.props.localization.messages});
        }
    }

    render() {

        return <div className='wrapper'>
            <Header messages={this.state.strings}/>
            <main className='main'>
                <Container>
                    <Row>
                        <Col md='12' xs='12' lg='12'>
                            <div className='access-denied'>
                                <img src={NotFoundImg} className='access-denied__img'/>
                            </div>
                        </Col>
                    </Row>
                </Container>
            </main>
        </div>
    }
}

function mapStateToProps(state) {
    return {
        localization: state.localization,
    }
}

function mapDispatchToProps(dispatch) {
    return {
        getMessagesAction: bindActionCreators(getMessagesAction, dispatch),
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(NotFound)