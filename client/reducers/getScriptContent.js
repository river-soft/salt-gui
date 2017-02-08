import {
    SCRIPT_CONTENT_SUCCESS,
    SCRIPT_CONTENT_FAIL
} from '../constants/GetScriptContent';

const initialState = {
    script: '',
    error: ''
};

export default function getScriptContent(state = initialState, action) {

    switch (action.type) {
        case SCRIPT_CONTENT_SUCCESS:
            return {...state, script: action.payload, error: ''};
        case SCRIPT_CONTENT_FAIL:
            return {...state, error: action.payload};
        default:
            return state
    }
}