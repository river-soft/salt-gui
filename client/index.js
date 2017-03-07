import React from 'react';
import 'babel-polyfill';
import {Provider} from 'react-redux';
import {Router, Route, hashHistory} from 'react-router';
import {render} from 'react-dom';
import App from './containers/App';
import Minions from './containers/Minions';
import GroupsAndMinions from './containers/GroupsAndMinions';
import JobResults from './containers/JobResults';
import './styles/app.scss'
import configureStore from './store/configureStore'

export const store = configureStore();

render(
    <Provider store={store}>
        <Router history={hashHistory}>
            <Route path='/' component={App}/>
            <Route path='/minions' component={Minions}/>
            <Route path='/groups-and-minions' component={GroupsAndMinions}/>
            <Route path='/job-results' component={JobResults}/>
        </Router>
    </Provider>,
    document.getElementById('root')
);