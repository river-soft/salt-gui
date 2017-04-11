import cookie from 'react-cookie';

export default function clone(obj) {

    let copy;

    // Handle the 3 simple types, and null or undefined
    if (null == obj || 'object' != typeof obj) return obj;

    // Handle Date
    if (obj instanceof Date) {
        copy = new Date();
        copy.setTime(obj.getTime());
        return copy;
    }

    // Handle Array
    if (obj instanceof Array) {
        copy = [];
        for (let i = 0, len = obj.length; i < len; i++) {
            copy[i] = clone(obj[i]);
        }
        return copy;
    }

    // Handle Object
    if (obj instanceof Object) {
        copy = {};
        for (let attr in obj) {
            if (obj.hasOwnProperty(attr)) copy[attr] = clone(obj[attr]);
        }
        return copy;
    }

    throw new Error('Unable to copy obj! Its type isnt supported.');
}

export function containsRole(roles, checkRoles) {

    for (let i = 0; i < roles.length; i++) {
        for (let j = 0; j < checkRoles.length; j++) {
            if (roles[i].trim() === checkRoles[j].trim()) {
                return true
            }
        }
    }

    return false
}

export function checkAuth(nextState, replace) {

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
}

export function checkRole(roles, replace) {

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
            pathname: '/access-denied'
        });
    }
}