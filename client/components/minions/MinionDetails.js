import React, {Component} from 'react';
import Panel from 'muicss/lib/react/panel';

export default class MinionDetails extends Component {

    parseJson(details) {

        let template = [];

        for (let key in details) {
            if (details.hasOwnProperty(key)) {
                template.push(<ul className='mui-list--unstyled' key={key}>
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
            template = this.parseJson(details),
            block = <div>
                <div className='minion-details__actions'>
                    <span className='file__actions_remove file__actions_item green' onClick={() => {
                        ::this.props.runScript(this.props.minionName);
                    }} title='запустить'><i className='mi mi-play-circle-filled'></i></span>
                    <span className='minion-details__action' onClick={() => {
                        this.props.getGroups(this.props.minionName);
                    }} title='редактировать группы'><i className='mi mi-create'></i></span>
                </div>
                {template}
            </div>;

        return <Panel className='minion-details'>
            {!this.props.error && !details ? 'Ожидается ответ от миньона...' : null}
            {this.props.error ? <span className='input_error'>{this.props.error.message}</span> : null}
            {details ? block : null}
        </Panel>
    }
}