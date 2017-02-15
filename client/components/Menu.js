import React, {Component} from 'react';
import {Link} from 'react-router';

export default class Menu extends Component {

    render() {

        return <div className='header__navigation'>
            <Link to='/' className='header__navigation_link'>Home</Link>
            <Link to='/minions' className='header__navigation_link'>Minions</Link>
            <Link to='/groups-and-minions' className='header__navigation_link'>Group and Minions</Link>
        </div>
    }
}
