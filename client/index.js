import React from 'react';
import 'babel-polyfill';
import {Provider} from 'react-redux';
import {Router, Route, hashHistory} from 'react-router';
import { render } from 'react-dom';
import App from './containers/App';
import Minions from './containers/Minions';
import './styles/app.scss'
import configureStore from './store/configureStore'
import io from 'socket.io-client';

export const store = configureStore();
store.dispatch({
    type: 'SET_STATE',
    state: {
        vote: {
            pair: ['Sunshine', '28 Days Later'],
            tally: {Sunshine: 2}
        }
    }
});

export const socket = io(`${location.protocol}//${location.hostname}:8081`);
socket.on('state', state =>
    store.dispatch({type: 'SET_STATE', state})
);

render(
    <Provider store={store}>
        <Router history={hashHistory}>
            <Route path='/' component={App}/>
            <Route path='/minions' component={Minions}/>
        </Router>
    </Provider>,
    document.getElementById('root')
);