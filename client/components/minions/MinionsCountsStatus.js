import React, {Component} from 'react';

export default class MinionsCountsStatus extends Component {

    render() {

        let countsStatus = this.props.countsStatus;

        return <ul className='mui-list--unstyled minions-list'>
            {Object.keys(countsStatus).map((key, index) => {
                return <li className='minions-list__item' key={index}><span className='mui--pull-left'>{key}</span><span className='mui--pull-right'>{countsStatus[key]}</span></li>
            })}
        </ul>
    }
}