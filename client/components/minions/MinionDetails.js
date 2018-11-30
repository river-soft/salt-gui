import React, {Component} from 'react';
import Panel from 'muicss/lib/react/panel';
import {containsRole} from '../../helpers';

export default class MinionDetails extends Component {

    constructor(props) {
        super(props);

        this.state = {
            interval: 0
        }
    }

    componentDidUpdate() {
        if (this.props.error || this.props.details) {
            clearInterval(this.state.interval);
        }
    }

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

    loading(el) {

        if (!el) {
            clearInterval(this.state.interval)
        } else {
            this.state.interval = setInterval(() => {

                if (el.innerHTML.length === 5) {
                    el.innerHTML = '';
                } else {
                    el.innerHTML = el.innerHTML + '.';
                }
            }, 500);
        }
    }


    render() {

        let details = this.props.details,
            template = this.parseJson(details),
            messages = this.props.messages,
            block = <div>
                <div className='minion-details__actions'>

                    {/*{containsRole(this.props.user.roles, ['ROLE_EXECUTE_SCRIPTS_ON_MINION', 'ROLE_ROOT']) ?*/}
                        {/*<span className='file__actions_remove file__actions_item green' onClick={() => {*/}
                            {/*::this.props.runScript(this.props.minionName);*/}
                        {/*}} title={messages['client.btn.execute']}><i className='mi mi-play-circle-filled'></i></span>*/}
                        {/*: null}*/}

                    {containsRole(this.props.user.roles, ['ROLE_EDIT_GROUPS_OF_MINION', 'ROLE_ROOT']) ?
                        <span className='minion-details__action' onClick={() => {
                            this.props.getGroups(this.props.minionName);
                        }} title={messages['client.btn.edit.groups']}><i className='mi mi-create'></i></span>
                        : null}

                </div>
                {template}
            </div>,
            errorSpan = this.refs['error'];

        if (this.props.error) {
            if (!errorSpan.innerHTML && this.props.error.message.indexOf(this.props.minionName) + 1) {
                errorSpan.innerHTML = this.props.error.message;
            }
        } else {
            if (errorSpan) {
                errorSpan.innerHTML = '';
            }
        }

        return <Panel className='minion-details'>
            {(errorSpan && !errorSpan.innerHTML.length || !errorSpan) && !details ? messages['client.message.waiting.answer'] : null}
            {(errorSpan && !errorSpan.innerHTML.length || !errorSpan) && !details ?
                <span ref={(el) => {
                    ::this.loading(el);
                }}>...</span> : null}
            <span className='input_error' ref='error'></span>
            {details ? block : null}
        </Panel>
    }
}