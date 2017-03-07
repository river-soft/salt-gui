import React, {Component} from 'react';
import AppBar from 'muicss/lib/react/appbar';
import Menu from '../components/Menu';
import {Link} from 'react-router';

export class Header extends Component {

    render() {
        return <AppBar className='header'>
            <div className='header__logo'>
                <Link to='/' className='header__navigation_link' />
            </div>
            <Menu/>
        </AppBar>

    }
}