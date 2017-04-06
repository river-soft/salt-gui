import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Header} from '../components/Header';
import Container from 'muicss/lib/react/container';
import Col from 'muicss/lib/react/col';
import Row from 'muicss/lib/react/row';
import NotFoundImg from '../styles/images/not-found.png';

class NotFound extends Component {

    render() {

        return <div className='wrapper'>
            <Header/>
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

export default connect()(NotFound)