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
import AccessDenied from './containers/AccessDenied';
import NotFound from './containers/NotFound';
import './styles/app.scss';
import configureStore from './store/configureStore';
import {checkAuth, checkRole} from './helpers';
import LocalizedStrings from 'react-localization';
import $ from 'jquery';
import {GET_MESSAGES_SUCCESS} from './constants/GetMessages';

export const store = configureStore();

$.get('/bundle-messages', data => {

    let strings = new LocalizedStrings(data);

    strings.setLanguage('en');

    store.dispatch({
        type: GET_MESSAGES_SUCCESS,
        payload: strings
    });

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
                <Route path='/access-denied' component={AccessDenied}/>
                <Route path='*' component={NotFound}/>
            </Router>
        </Provider>,
        document.getElementById('root')
    );

});