import {
    DELETE_MINIONS_SUCCESS,
    DELETE_MINIONS_FAIL
} from '../constants/DeleteMinions';

const initialState = {
    deleted: false,
    error: ''
};

export default function deleteMinions(state = initialState, action) {

    switch (action.type) {
        case DELETE_MINIONS_SUCCESS:
            return {...state, deleted: true, error: ''};
        case DELETE_MINIONS_FAIL:
            return {...state, error: action.payload};
        default:
            return state
    }
}