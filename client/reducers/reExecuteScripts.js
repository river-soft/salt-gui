import {
    REEXECUTE_SCRIPTS_REQUEST,
    REEXECUTE_SCRIPTS_SUCCESS,
    REEXECUTE_SCRIPTS_FAIL
} from '../constants/ReExecuteScripts';

const initialState = {
    reExecute: false,
    error: ''
};

export default (state = initialState, action) => {

    switch (action.type) {
        case REEXECUTE_SCRIPTS_REQUEST:
            return {...state, error: ''};
        case REEXECUTE_SCRIPTS_SUCCESS:
            return {...state, reExecute: true, error: ''};
        case REEXECUTE_SCRIPTS_FAIL:
            return {...state, reExecute: false, error: action.payload};
        default:
            return state;
    }
}