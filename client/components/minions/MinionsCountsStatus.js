import React, {Component} from 'react';

export default class MinionsCountsStatus extends Component {

    render() {

        let countsStatus = this.props.countsStatus;

        return <div className='block-list'>
            <h6 className='block-list__header'>Статус</h6>
            <ul className='mui-list--unstyled minions-list'>
                {Object.keys(countsStatus).map((key, index) => {
                    return <li className='minions-list__item' key={index}><span
                        className='mui--pull-left'>{key}</span><span
                        className='mui--pull-right'>{countsStatus[key]}</span></li>
                })}
            </ul>
        </div>
    }
}