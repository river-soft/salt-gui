import React, {Component} from 'react';
import {Link} from 'react-router';

export default class Menu extends Component {

    render() {

        return <div className='header__navigation'>
            <Link to='/' className='header__navigation_link' activeClassName='active'>Главная</Link>
            <Link to='/scripts' className='header__navigation_link' activeClassName='active'>Скрипты</Link>
            <Link to='/groups-and-minions' className='header__navigation_link' activeClassName='active'>Миньоны</Link>
            <Link to='/job-results' className='header__navigation_link' activeClassName='active'>Работы</Link>
        </div>
    }
}
