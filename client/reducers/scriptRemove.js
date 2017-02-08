import {
    SCRIPT_REMOVE_SUCCESS,
    SCRIPT_REMOVE_FAIL
} from '../constants/ScriptRemove';

const initialState = {
    removed: false,
    error: ''
};

export default (state = initialState, action) => {

    switch(action.type) {
        case SCRIPT_REMOVE_SUCCESS:
            return {...state, removed: true, error: ''};
        case SCRIPT_REMOVE_FAIL:
            return {...state, error: action.payload};
        default:
            return state
    }
}