import React, {Component} from 'react';
import Panel from 'muicss/lib/react/panel';

export default class JobResultDetails extends Component {

    constructor(props) {
        super(props);

        this.state = {
            minimize: false
        }
    }

    toggleMinimize() {
        this.setState({minimize: !this.state.minimize});
    }

    parseObj(obj) {

        let template = [];

        for (let key in obj) {
            if (obj.hasOwnProperty(key)) {
                if (key === 'id' || key === 'lastModifiedDate' || key === 'jobName') {
                    continue
                } else {
                    template.push(
                        <p>
                            {key}: {obj[key]}
                        </p>
                    )
                }
            }
        }

        return template
    }

    render() {

        return <Panel className='posr'>
            <span className='turn-off' onClick={::this.toggleMinimize} title={this.state.minimize ? 'Развернуть' : 'Свернуть'}>
                <i className={this.state.minimize ? 'mi mi-add' : 'mi mi-remove'}></i>
            </span>
            <div className={this.state.minimize ? 'hidden' : ''}>{this.parseObj(this.props.result)}</div>
        </Panel>
    }
}