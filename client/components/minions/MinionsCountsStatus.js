import React, {Component} from 'react';

export default class MinionsCountsStatus extends Component {

    render() {

        let countsStatus = this.props.countsStatus,
            messages = this.props.messages;

        return <div className='block-list'>
            <h6 className='block-list__header'>{this.props.messages['client.minions.status']}</h6>
            <ul className='mui-list--unstyled minions-list'>
                {Object.keys(countsStatus).map((key, index) => {

                    let lastKey = key.split('.'),
                        lastKeyElement = lastKey.length - 1;

                    return <li className='minions-list__item' key={index}><span
                        className={'mui--pull-left minions-list__item_' + lastKey[lastKeyElement].toLowerCase()}>{messages[key]}</span><span
                        className={'mui--pull-right minions-list__item_' + lastKey[lastKeyElement].toLowerCase()}>{countsStatus[key]}</span></li>
                })}
            </ul>
        </div>
    }
}