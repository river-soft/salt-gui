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

                    if (obj[key]) {

                        template.push(
                            <p>
                                {key}: {obj[key]}
                            </p>
                        )
                    }
                }
            }
        }

        return template
    }

    parseObjName(obj) {

        if (obj.hasOwnProperty('cmd')) {

            if (obj['cmd']) {
                return <p>{'cmd'}: {obj['cmd']}</p>;

            } else {

                if (obj.hasOwnProperty('comment')) {

                    return <p>{'comment'}: {obj['comment']}</p>;
                }
            }
        }
    }

    render() {

        return <Panel className='posr'>
            <span className='turn-off' onClick={::this.toggleMinimize}
                  title={this.state.minimize ? 'Развернуть' : 'Свернуть'}>
                <i className={this.state.minimize ? 'mi mi-add' : 'mi mi-remove'}></i>
            </span>
            <div>
                {/*{className={this.state.minimize ? 'hidden' : ''}> }*/}
                {!this.state.minimize ? this.parseObj(this.props.result) : this.parseObjName(this.props.result)}
            </div>
        </Panel>
    }
}