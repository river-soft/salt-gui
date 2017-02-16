import React, {Component} from 'react';
import AppBar from 'muicss/lib/react/appbar';
import Menu from '../components/Menu';

export class Header extends Component {

    render() {

        return <AppBar className='header'>
                <Menu/>
            </AppBar>

    }
}