import React, {Component} from 'react';
import {Link} from 'react-router';
import {containsRole} from '../helpers';
import cookie from 'react-cookie';
import Dropdown from 'muicss/lib/react/dropdown';
import DropdownItem from 'muicss/lib/react/dropdown-item';

export default class Menu extends Component {

    changeLocale(locale) {
        this.props.setLanguage(locale);
    }

    render() {

        let token = cookie.load('accessToken'),
            user = token ? JSON.parse(atob(token)) : null,
            messages = this.props.messages;

        if (user && typeof user.roles === 'string') {
            user.roles = user.roles.replace(/[\[\]]/g, '').split(',');
        }

        return <div className='header__logo_navigation'>
            <div className='header__logo_navigation-block'>
                {user ? <span className='header__logo_navigation-user'>{user.userName}</span> : null}
                {user ? <a href='/logout' className='header__logo_navigation-logout'>
                        <i className='mi mi-cancel'></i>
                    </a> : null}
                <Dropdown alignMenu='right' label={cookie.load('locale')} className='header__logo_navigation-locale'>
                    <DropdownItem onClick={() => {
                        ::this.changeLocale('en')
                    }}>en</DropdownItem>
                    <DropdownItem onClick={() => {
                        ::this.changeLocale('ru')
                    }}>ru</DropdownItem>
                    <DropdownItem onClick={() => {
                        ::this.changeLocale('ua')
                    }}>ua</DropdownItem>
                </Dropdown>
            </div>
            {user ? <div className='header__logo_navigation-menu'>
                    {containsRole(user.roles, ['ROLE_PAGE_MAIN', 'ROLE_ROOT']) ?
                        <Link to='/' className='header__logo_navigation-link'
                              activeClassName='active'>{messages['client.menu.main']}</Link> : null}
                    {containsRole(user.roles, ['ROLE_PAGE_SCRIPTS', 'ROLE_ROOT']) ?
                        <Link to='/scripts' className='header__logo_navigation-link'
                              activeClassName='active'>{messages['client.menu.scripts']}</Link> : null}
                    {containsRole(user.roles, ['ROLE_PAGE_GROUPS_AND_MINIONS', 'ROLE_ROOT']) ?
                        <Link to='/groups-and-minions' className='header__logo_navigation-link'
                              activeClassName='active'>{messages['client.menu.minions']}</Link> : null}
                    {containsRole(user.roles, ['ROLE_PAGE_JOB_RESULTS', 'ROLE_ROOT']) ?
                        <Link to='/job-results' className='header__logo_navigation-link'
                              activeClassName='active'>{messages['client.menu.jobs']}</Link> : null}
                </div> : null}
        </div>
    }
}
