import {
    EDIT_MINION_GROUPS_SUCCESS,
    EDIT_MINION_GROUPS_FAIL
} from '../constants/EditMinionGroups';

const initialState = {
    editGroups: false,
    error: ''
};

export default function editMinionGroups(state = initialState, action) {

    switch (action.type) {
        case EDIT_MINION_GROUPS_SUCCESS:
            return {...state, editGroup: true, error: ''};
        case EDIT_MINION_GROUPS_FAIL:
            return {...state, error: action.payload};
        default:
            return state
    }
}