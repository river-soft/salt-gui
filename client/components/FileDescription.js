import React, {Component} from 'react';
import Panel from 'muicss/lib/react/panel';

export default class FileDescription extends Component {

    constructor(props) {
        super(props);

        this.state = {
            content: ''
        }
    }

    render() {

        let description = this.props.scriptContent;

        return (
            <Panel>
                <div className='file-name'>{description.script.name}</div>
                <pre className='file-description'>
                    {description.script.content}
                </pre>
            </Panel>
        )
    }
}