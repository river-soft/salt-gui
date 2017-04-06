import React from 'react';
import 'babel-polyfill';
import {Provider} from 'react-redux';
import {Router, Route, hashHistory} from 'react-router';
import {render} from 'react-dom';
import App from './containers/App';
import Minions from './containers/Minions';
import GroupsAndMinions from './containers/GroupsAndMinions';
import JobResults from './containers/JobResults';
import Authorization from './containers/Authorization';
import './styles/app.scss';
import configureStore from './store/configureStore';
import {containsRole} from './helpers';
import cookie from 'react-cookie';

export const store = configureStore();

const checkAuth = (nextState, replace) => {

    let token = cookie.load('accessToken'),
        user = token ? JSON.parse(atob(token)) : null,
        unAuthorized = window.unAuthorized;

    if (unAuthorized) {
        cookie.remove('accessToken', {path: '/'});
        window.unAuthorized = false;
    }

    if (!user) {
        replace({
            pathname: '/login'
        })
    }
};

const checkRole = (roles, replace) => {

    let token = cookie.load('accessToken'),
        user = token ? JSON.parse(atob(token)) : null;

    if (!user) {
        replace({
            pathname: '/login'
        });
        return
    }

    if (typeof user.roles === 'string') {
        user.roles = user.roles.replace(/[\[\]]/g, '').split(',');
    }

    let contains = containsRole(user.roles, roles);

    if (!contains) {
        replace({
            pathname: '/login'
        });
    }
};

render(
    <Provider store={store}>
        <Router history={hashHistory}>
            <Route path='/login' component={Authorization}/>
            <Route path='/' onEnter={(nextState, replace) => {
                checkAuth(nextState, replace);
                checkRole(['ROLE_PAGE_MAIN', 'ROLE_ROOT'], replace);
            }} component={Minions}/>
            <Route path='/scripts' onEnter={(nextState, replace) => {
                checkAuth(nextState, replace);
                checkRole(['ROLE_PAGE_SCRIPTS', 'ROLE_ROOT'], replace);
            }} component={App}/>
            <Route path='/groups-and-minions' onEnter={(nextState, replace) => {
                checkAuth(nextState, replace);
                checkRole(['ROLE_PAGE_GROUPS_AND_MINIONS', 'ROLE_ROOT'], replace);
            }} component={GroupsAndMinions}/>
            <Route path='/job-results' onEnter={(nextState, replace) => {
                checkAuth(nextState, replace);
                checkRole(['ROLE_PAGE_JOB_RESULTS', 'ROLE_ROOT'], replace);
            }} component={JobResults}/>
        </Router>
    </Provider>,
    document.getElementById('root')
);