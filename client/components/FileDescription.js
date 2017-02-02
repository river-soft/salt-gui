import React, {Component} from 'react';
import Panel from 'muicss/lib/react/panel';

export default class FileDescription extends Component {

    render() {

        let description = this.props.description;

        return (
            <Panel>
                <pre className='file-description'>
                    {description}
                </pre>
            </Panel>
        )
    }
}