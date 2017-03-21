import React, {Component} from 'react';
import AppBar from 'muicss/lib/react/appbar';
import Menu from '../components/Menu';
import {Link} from 'react-router';
import Container from 'muicss/lib/react/container';
import Col from 'muicss/lib/react/col';
import Row from 'muicss/lib/react/row';

export class Header extends Component {

    render() {
        return <AppBar className='header'>
            <div className='header__logo'>
                <Link to='/' className='header__navigation_link' />
            </div>
            <Menu/>
            <div className='header__bottom'>
                <Container>
                    <Row>
                        <Col md='12' xs='12' lg='12'>
                            <h1>{this.props.header}</h1>
                        </Col>
                    </Row>
                </Container>
            </div>
        </AppBar>

    }
}