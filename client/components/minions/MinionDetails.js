import React, {Component} from 'react';
import Panel from 'muicss/lib/react/panel';

export default class MinionDetails extends Component {

    parseJson(details) {

        let template = [];

        for (let key in details) {
            if (details.hasOwnProperty(key)) {
                template.push(<ul className='mui-list--unstyled'>
                    <li className='object'>
                        <div className='object__key'>{key}:</div>
                        <div className='object__value'>
                        {Object.prototype.toString.call(details[key]) === '[object Object]' ?
                            this.parseJson(details[key]) :
                            Object.prototype.toString.call(details[key]) === '[object Array]' ?
                                <ul className='mui-list--unstyled'>
                                    {details[key].map((el, i) => {
                                        return <li key={i}>{el}</li>
                                    })}
                                </ul> :
                                details[key]}
                    </div>
                    </li>
                </ul>);
            }
        }

        return template
    }

    render() {

        let details = this.props.details,
            template = this.parseJson(details);

        return <Panel>
            {template}
        </Panel>
    }
}