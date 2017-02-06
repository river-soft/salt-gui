import {
    GROUP_CREATE_SUCCESS,
    GROUP_CREATE_FAIL
} from '../constants/GroupCreate';

const initialState = {
    group: '',
    error: ''
};

export default (state = initialState, action) => {

    switch (action.type) {
        case GROUP_CREATE_SUCCESS:
            return {...state, group: action.payload, error: ''};
        case GROUP_CREATE_FAIL:
            return {...state, error: action.payload.message};
        default:
            return state;
    }
}

