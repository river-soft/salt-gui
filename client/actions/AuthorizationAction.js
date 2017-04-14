import {
    AUTHORIZATION_REQUEST,
    AUTHORIZATION_SUCCESS,
    AUTHORIZATION_FAIL
} from '../constants/Authorization';
import $ from 'jquery';
import {hashHistory} from 'react-router';
import cookie from 'react-cookie';
import {containsRole} from '../helpers';

export function authorization(userName, password) {

    return dispatch => {

        dispatch({
            type: AUTHORIZATION_REQUEST
        });

        $.ajax({
            url: '/login',
            type: 'post',
            data: {
                username: userName,
                password: password
            },
            headers: {
                'Authorization': 'Basic' + btoa(userName + ':' + password)
            },
            success: (res, status, response) => {

                let user = {
                        userName: response.getResponseHeader('userName'),
                        roles: response.getResponseHeader('roles'),
                        expiryDate: new Date().getTime()
                    },
                    pages = [
                        {
                            url: '/',
                            permissions: ['ROLE_ROOT', 'ROLE_PAGE_MAIN']
                        },
                        {
                            url: '/scripts',
                            permissions: ['ROLE_ROOT', 'ROLE_PAGE_SCRIPTS']
                        },
                        {
                            url: '/groups-and-minions',
                            permissions: ['ROLE_ROOT', 'ROLE_PAGE_GROUPS_AND_MINIONS']
                        },
                        {
                            url: '/job-results',
                            permissions: ['ROLE_ROOT', 'ROLE_PAGE_JOB_RESULTS']
                        },
                    ],
                    goToPage = false;

                cookie.save('accessToken', btoa(JSON.stringify(user)));

                dispatch({
                    type: AUTHORIZATION_SUCCESS,
                    payload: status
                });

                const redirect = (user, containsRoles, redirectUrl) => {

                    if (typeof user.roles === 'string') {
                        user.roles = user.roles.replace(/[\[\]]/g, '').split(',');
                    }

                    if (containsRole(user.roles, containsRoles)) {
                        hashHistory.push(redirectUrl);
                        goToPage = true;
                    }

                    return false;
                };

                for (let i = 0; i < pages.length; i++) {
                    if (!goToPage) {
                        redirect(user, pages[i].permissions, pages[i].url)
                    }
                }
            },
            error: error => {
                dispatch({
                    type: AUTHORIZATION_FAIL,
                    payload: error,
                    error: true
                });
            }
        });
    }
}