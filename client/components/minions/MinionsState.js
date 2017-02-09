import React, {Component} from 'react';

export default class MinionsState extends Component {

    render() {

        let minionsStates = this.props.minionsStates;
        if(typeof minionsStates === 'undefined') {
            console.log('FAIL')
        }

        return <ul className='mui-list--unstyled minions-list'>
            {minionsStates ? minionsStates.map((item, index) => {
                return <li className='minions-list__item' key={index}><span className='mui--pull-left'>{item[0]}</span><span className='mui--pull-right'>{item[1]}</span></li>
            }) : null}
        </ul>
    }
}