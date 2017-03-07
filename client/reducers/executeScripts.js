import {
    EXECUTE_SCRIPTS_SUCCESS,
    EXECUTE_SCRIPTS_FAIL
} from '../constants/ExecuteScripts';

const initialState = {
    execute: false,
    error: ''
};

export default (state = initialState, action) => {

    switch (action.type) {
        case EXECUTE_SCRIPTS_SUCCESS:
            return {...state, execute: true, error: ''};
        case EXECUTE_SCRIPTS_FAIL:
            return {...state, execute: false, error: action.payload};
        default:
            return state;
    }
}