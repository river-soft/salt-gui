import {
    AUTHORIZATION_SUCCESS,
    AUTHORIZATION_FAIL
} from '../constants/Authorization';

const initialState = {
    auth: '',
    error: ''
};

export default (state = initialState, action) => {

    switch (action.type) {
        case AUTHORIZATION_SUCCESS:
            return {...state, auth: action.payload, error: ''};
        case AUTHORIZATION_FAIL:
            return {...state, error: action.payload};
        default:
            return state;
    }
}