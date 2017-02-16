import React, {Component} from 'react';

export default class MinionsCountsGroup extends Component {

    render() {

        let countsGroup = this.props.countsStatus;


        return <div className='block-list'>
            <h6 className='block-list__header'>Groups</h6>
            <ul className='mui-list--unstyled minions-list'>
                {Object.keys(countsGroup).map((key, index) => {
                    return <li className='minions-list__item' key={index}><span
                        className='mui--pull-left'>{key}</span><span
                        className='mui--pull-right'>{countsGroup[key]}</span></li>
                })}
            </ul>
        </div>
    }
}