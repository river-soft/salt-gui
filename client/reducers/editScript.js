import {
    EDIT_SCRIPT_SUCCESS,
    EDIT_SCRIPT_FAIL
} from '../constants/EditScript';

const initialState = {
    edit: '',
    script: '',
    error: ''
};

export default (state = initialState, action) => {

    switch (action.type) {
        case EDIT_SCRIPT_SUCCESS:
            return {...state, edit: true, script: action.payload, error: ''};
        case EDIT_SCRIPT_FAIL:
            return {...state, edit: false, error: action.payload};
        default:
            return state;
    }
}