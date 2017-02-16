import {
    GROUP_EDIT_SUCCESS,
    GROUP_EDIT_FAIL
} from '../constants/GroupEdit';

const initialState = {
    edit: false,
    error: ''
};

export default function editGroup(state = initialState, action) {

    switch (action.type) {
        case GROUP_EDIT_SUCCESS:
            return {...state, edit: true, error: ''};
        case GROUP_EDIT_FAIL:
            return {...state, error: action.payload};
        default:
            return state;
    }
}