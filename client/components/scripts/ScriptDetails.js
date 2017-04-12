import React, {Component} from 'react';
import Panel from 'muicss/lib/react/panel';
import {containsRole} from '../../helpers';

export default class ScriptDetails extends Component {

    render() {

        let description = this.props.scriptContent, messages = this.props.messages;

        return (
            <Panel className='file'>
                <div className='file__actions'>

                    {containsRole(this.props.user.roles, ['ROLE_EXECUTE_SCRIPT_ON_MINIONS', 'ROLE_ROOT']) ?
                        <span className='file__actions_remove file__actions_item green' onClick={() => {
                            this.props.runScript(description.script.name);
                        }} title={messages['client.btn.execute']}><i className='mi mi-play-circle-filled'></i></span>
                        : null}

                    {containsRole(this.props.user.roles, ['ROLE_EDIT_SCRIPT', 'ROLE_ROOT']) ?
                        <span className='file__actions_edit file__actions_item' onClick={() => {
                            this.props.editScript(description.script, description.script.content);
                        }} title={messages['client.btn.edit']}><i className='mi mi-edit'></i></span>
                        : null}

                    {containsRole(this.props.user.roles, ['ROLE_DELETE_SCRIPT', 'ROLE_ROOT']) ?
                    <span className='file__actions_remove file__actions_item' onClick={() => {
                        this.props.removeScript(description.script);
                    }} title={messages['client.btn.delete']}><i className='mi mi-delete'></i></span>
                        : null}
                </div>
                <div className='file-name'>{description.script.name}</div>
                <pre className='file-description'>
                    {description.script.content}
                </pre>
            </Panel>
        );
    }
}
