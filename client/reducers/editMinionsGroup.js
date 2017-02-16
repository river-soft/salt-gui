import {
    EDIT_MINIONS_GROUP_SUCCESS,
    EDIT_MINIONS_GROUP_FAIL
} from '../constants/EditMinionsGroup';

const initialState = {
    edit: false,
    error: ''
};

export default function editMinionsGroup(state = initialState, action) {

    switch (action.type) {
        case EDIT_MINIONS_GROUP_SUCCESS:
            return {...state, edit: true, error: ''};
        case EDIT_MINIONS_GROUP_FAIL:
            return {...state, error: action.payload};
        default:
            return state
    }
}
